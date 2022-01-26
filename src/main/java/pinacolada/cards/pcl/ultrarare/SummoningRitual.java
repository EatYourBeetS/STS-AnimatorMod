package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll_Player;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.vfx.megacritCopy.HemokinesisEffect2;
import pinacolada.powers.PCLPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class SummoningRitual extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(SummoningRitual.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public SummoningRitual()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, -1);

        SetAffinity_Dark(1);

        SetRetain(true);
        SetPurge(true, false);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyPower(new UnnamedDollPlayerPower(p, cardData));
    }

    public static class UnnamedDollPlayerPower extends PCLPower
    {
        private TheUnnamed_Doll_Player doll;

        public UnnamedDollPlayerPower(AbstractCreature owner, PCLCardData data)
        {
            super(owner, data);

            this.amount = 0;
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            amount = player.currentHealth / 2;
            updateDescription();

            float x = owner.drawX + (180f * Settings.scale);
            float y = owner.drawY + (owner.hb_h * 0.5f) + (180f * Settings.scale);

            PCLActions.Bottom.LoseHP(amount, AttackEffects.NONE);
            PCLActions.Bottom.VFX(new HemokinesisEffect2(owner.hb.cX, owner.hb.cY, x, y))
            .AddCallback(new TheUnnamed_Doll_Player(x, y), (c, __) ->
            {
                this.doll = c;
                this.doll.rollMove();
                this.doll.showHealthBar();
                this.doll.increaseMaxHp(amount, true);
            })
            .SetDuration(0.5f, true);
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(0);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (doll != null)
            {
                doll.takeTurn();
            }
        }

        @Override
        public void update(int slot)
        {
            super.update(slot);

            if (doll != null)
            {
                doll.update();
                GR.UI.AddPreRender(doll::render);
            }
        }

        @Override
        public void onVictory()
        {
            super.onVictory();

            if (owner.currentHealth > 0)
            {
                owner.heal(amount);
                amount = 0;
            }
        }

        @Override
        public int onLoseHp(int damageAmount)
        {
            if (doll != null)
            {
                final int playerDamage = damageAmount / 2;
                doll.damage(new DamageInfo(null, damageAmount - playerDamage, DamageInfo.DamageType.HP_LOSS));
                return super.onLoseHp(playerDamage);
            }

            return super.onLoseHp(damageAmount);
        }
    }
}