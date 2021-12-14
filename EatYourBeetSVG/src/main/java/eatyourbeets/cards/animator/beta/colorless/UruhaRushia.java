package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UruhaRushia extends AnimatorCard implements OnOrbApplyFocusSubscriber
{
    public static final EYBCardData DATA = Register(UruhaRushia.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(2).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Hololive);

    public UruhaRushia()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetRetainOnce(true);
        SetExhaust(true);

        SetAffinity_Blue(1);
    }

    public void OnUpgrade() {
        SetRetain(true);
        SetHaste(true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onOrbApplyFocus.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new UruhaRushiaPower(p, this.secondaryValue));
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        if (player.hand.contains(this) && orb != null && orb == GameUtilities.GetFirstOrb(null)) {
            orb.passiveAmount += magicNumber;
            orb.evokeAmount += magicNumber;
        }
    }

    public static class UruhaRushiaPower extends AnimatorPower
    {
        public UruhaRushiaPower(AbstractPlayer owner, int amount)
        {
            super(owner, UruhaRushia.DATA);

            this.amount = amount;
            this.priority = 99;

            updateDescription();
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
            if (GameUtilities.IsPlayerTurn() && info.type == DamageInfo.DamageType.NORMAL || info.type == DamageInfo.DamageType.THORNS) {
                return super.onAttackedToChangeDamage(info, 0);
            }
            return super.onAttackedToChangeDamage(info, damageAmount);
        }


        @Override
        public float atDamageFinalReceive(float damage, DamageInfo.DamageType type)
        {
            if (GameUtilities.IsPlayerTurn() && type == DamageInfo.DamageType.NORMAL || type == DamageInfo.DamageType.THORNS) {
                return super.atDamageFinalReceive(0f, type);
            }
            return super.atDamageFinalReceive(damage, type);
        }

        @Override
        public void atEndOfRound()
        {
            ReducePower(1);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}