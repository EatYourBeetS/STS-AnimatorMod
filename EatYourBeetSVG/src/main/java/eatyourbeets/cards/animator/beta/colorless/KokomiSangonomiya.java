package eatyourbeets.cards.animator.beta.colorless;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;

public class KokomiSangonomiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KokomiSangonomiya.class).SetSkill(2, CardRarity.RARE).SetMaxCopies(1).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);
    public static final int LOSE_DEXTERITY = 99;

    public KokomiSangonomiya()
    {
        super(DATA);

        Initialize(0, 0, 6, 2);
        SetUpgrade(0, 0, -1, 1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetExhaust(true);
        SetHealing(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
            GameActions.Bottom.VFX(VFX.WaterDome(player.hb.cX,(player.hb.y+player.hb.cY)/2));
            GameActions.Bottom.SFX(SFX.ANIMATOR_WATER_DOME);
            GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Dexterity,-LOSE_DEXTERITY).IgnoreArtifact(true);
            GameActions.Bottom.StackPower(new KokomiSangonomiyaPower(p, secondaryValue, magicNumber));
    }

    public static class KokomiSangonomiyaPower extends AnimatorPower
    {
        public int secondaryAmount;

        public KokomiSangonomiyaPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, KokomiSangonomiya.DATA);

            this.priority = -99;
            this.secondaryAmount = secondaryAmount;

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, secondaryAmount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (GameUtilities.GetOrbCount(Water.ORB_ID) == 0) {
                GameActions.Bottom.LoseHP(secondaryAmount, AbstractGameAction.AttackEffect.NONE);
                Water waterOrb = new Water();
                waterOrb.IncreaseBasePassiveAmount(this.amount);
                GameActions.Bottom.ChannelOrb(waterOrb);
            }
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
        {
            final ArrayList<AbstractOrb> waterOrbs = new ArrayList<>();
            for (AbstractOrb orb : player.orbs)
            {
                if (Water.ORB_ID.equals(orb.ID) && orb.evokeAmount > 0)
                {
                    waterOrbs.add(orb);
                }
            }

            if (waterOrbs.size() > 0)
            {
                if (damageAmount > 0)
                {
                    damageAmount = AbsorbDamage(damageAmount, waterOrbs);
                }

                if (info.owner != null && info.owner.isPlayer != owner.isPlayer)
                {
                    GameActions.Bottom.DealDamage(owner, info.owner, damageAmount, DamageInfo.DamageType.THORNS, AttackEffects.WATER);
                    flashWithoutSound();
                }
            }

            return super.onAttackedToChangeDamage(info, damageAmount);
        }

        private int AbsorbDamage(int damage, ArrayList<AbstractOrb> waterOrbs)
        {
            final AbstractOrb next = waterOrbs.get(0);
            final float temp = damage;

            damage = Math.max(0, damage - next.evokeAmount);
            next.evokeAmount -= temp;

            if (next.evokeAmount <= 0)
            {
                waterOrbs.remove(next);
                next.evokeAmount = 0;
                GameActions.Top.Add(new EvokeSpecificOrbAction(next));
            }

            return (damage > 0 && waterOrbs.size() > 0) ? AbsorbDamage(damage, waterOrbs) : damage;
        }
    }
}