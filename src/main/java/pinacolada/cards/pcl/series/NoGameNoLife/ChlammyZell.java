package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.cards.pcl.special.ChlammyZell_Scheme;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class ChlammyZell extends PCLCard
{
    public static final PCLCardData DATA = Register(ChlammyZell.class)
            .SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ChlammyZell_Scheme(), false));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public ChlammyZell()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Orange(1);
        SetAffinity_Dark(1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Blue, 3);
        SetAffinityRequirement(PCLAffinity.Dark, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name,1,false).AddCallback(cards -> {
            for (AbstractCard card : cards) {
                PCLActions.Top.FetchFromPile(name, 1, player.discardPile)
                        .SetOptions(true, false)
                        .SetFilter(c -> c.cost == card.cost);
            }
        });

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_DrawNextTurn(CardType.ATTACK));
            choices.AddEffect(new GenericEffect_DrawNextTurn(CardType.SKILL));
            choices.AddEffect(new GenericEffect_DrawNextTurn(CardType.POWER));
            choices.AddEffect(new GenericEffect_DrawNextTurn(CardType.CURSE));
            choices.AddEffect(new GenericEffect_DrawNextTurn(CardType.STATUS));
        }
        choices.Select(1, m);

        if (TrySpendAffinity(PCLAffinity.Blue) && TrySpendAffinity(PCLAffinity.Dark) && info.TryActivateLimited())
        {
            PCLActions.Bottom.MakeCardInHand(new ChlammyZell_Scheme());
        }
    }

    protected static class GenericEffect_DrawNextTurn extends GenericEffect implements OnStartOfTurnPostDrawSubscriber
    {
        private final CardType cardType;

        public GenericEffect_DrawNextTurn(CardType cardType)
        {
            this.cardType = cardType;
        }

        @Override
        public String GetText()
        {
            return PCLJUtils.Format(ChlammyZell.DATA.Strings.EXTENDED_DESCRIPTION[0], StringUtils.capitalize(cardType.toString().toLowerCase()));
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }

        @Override
        public void OnStartOfTurnPostDraw() {
            PCLActions.Bottom.Draw(1).SetFilter(c -> c.type == cardType, false);
            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}