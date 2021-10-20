package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class WinryRockbell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(WinryRockbell.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();


    public WinryRockbell()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Steel();
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new WinryRockbellPower(p, magicNumber));
    }

    public static class WinryRockbellPower extends AnimatorClickablePower
    {
        public WinryRockbellPower(AbstractCreature owner, int amount)
        {
            super(owner, WinryRockbell.DATA, PowerTriggerConditionType.Energy, 1);

            triggerCondition.SetUses(1, false, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Bottom.UpgradeFromHand(name, amount, true)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    SFX.Play(SFX.CARD_UPGRADE, 1f, 1.1f);
                }
            });
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SelectFromHand(name, 1, false)
                    .SetOptions(true, true, true)
                    .SetMessage(RetainCardsAction.TEXT[0])
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            AbstractCard card = cards.get(0);
                            GameUtilities.Retain(card);
                        }
                    });
        }
    }
}