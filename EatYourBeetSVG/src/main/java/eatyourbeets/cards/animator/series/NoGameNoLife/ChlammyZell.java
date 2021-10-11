package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ChlammyZell_Scheme;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

public class ChlammyZell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChlammyZell.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ChlammyZell_Scheme(), false));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public ChlammyZell()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Water(1, 1, 0);
        SetAffinity_Earth(1);
        SetAffinity_Dark(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Water, 3);
        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name,1,false).AddCallback(cards -> {
            for (AbstractCard card : cards) {
                GameActions.Top.FetchFromPile(name, 1, player.discardPile)
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

        if (CheckAffinity(Affinity.Water) && CheckAffinity(Affinity.Dark) && info.TryActivateLimited())
        {
            GameActions.Bottom.MakeCardInHand(new ChlammyZell_Scheme());
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
            return JUtils.Format(ChlammyZell.DATA.Strings.EXTENDED_DESCRIPTION[0], StringUtils.capitalize(cardType.toString().toLowerCase()));
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }

        @Override
        public void OnStartOfTurnPostDraw() {
            GameActions.Bottom.Draw(1).SetFilter(c -> c.type == cardType, false);
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}