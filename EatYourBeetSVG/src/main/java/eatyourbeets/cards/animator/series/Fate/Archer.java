package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Archer extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Archer.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int COST = 7;

    public Archer()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 1, 0);

        SetAffinity_Green(1, 0, 0);
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new ArcherPower(p, magicNumber));
    }

    public class ArcherPower extends AnimatorClickablePower
    {

        public ArcherPower(AbstractCreature owner, int amount)
        {
            super(owner, Archer.DATA, PowerTriggerConditionType.Affinity, COST, null, null, Affinity.Green);

            Initialize(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (!isPlayer)
            {
                return;
            }

            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                final int debuffs = GameUtilities.GetDebuffsCount(m.powers);
                for (int i = 0; i < debuffs; i++)
                {
                    GameActions.Bottom.VFX(VFX.ThrowDagger(m.hb, 0.2f));
                    GameActions.Bottom.DealDamage(owner, m, amount, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                            .SetVFX(true, true);
                }
            }

            this.flash();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, COST);
        }

    }
}