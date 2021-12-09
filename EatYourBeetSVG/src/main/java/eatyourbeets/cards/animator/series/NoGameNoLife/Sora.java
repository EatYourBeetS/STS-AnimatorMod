package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.animator.special.Sora_Strategy1;
import eatyourbeets.cards.animator.special.Sora_Strategy2;
import eatyourbeets.cards.animator.special.Sora_Strategy3;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Sora extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Sora.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None, true)
            .SetSeries(CardSeries.NoGameNoLife)
            .PostInitialize(data -> data
                    .AddPreview(new Sora_Strategy1(), true)
                    .AddPreview(new Sora_Strategy2(), true)
                    .AddPreview(new Sora_Strategy3(), true)
                    .AddPreview(new Shiro(), false));
    private AbstractCard plan;

    public Sora()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1);

        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.addToBottom(new Sora_Strategy1());
        group.addToBottom(new Sora_Strategy2());
        group.addToBottom(new Sora_Strategy3());

        if (upgraded) {
            for (AbstractCard c : group.group) {
                c.upgrade();
            }
        }

        GameActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
                        plan = cards.get(0);
                    }
                });

        if (info.GetPreviousCardID().equals(Shiro.DATA.ID) && info.TryActivateLimited())
        {
            GameActions.Bottom.StackPower(new EnergizedPower(p, secondaryValue));
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (plan != null) {
            GameActions.Bottom.MakeCardInHand(plan).SetUpgrade(upgraded, false);
            GameUtilities.RefreshHandLayout();
        }
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        final AbstractCard last = GameUtilities.GetLastCardPlayed(true);
        return last != null && Shiro.DATA.ID.equals(last.cardID);
    }
}