package eatyourbeets.cards.animator.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.vfx.megacritCopy.HemokinesisEffect2;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll_SummoningRitual;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SummoningRitual extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SummoningRitual.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public SummoningRitual()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 0);

        SetAffinity_Dark(2);

        SetRetain(true);
        SetPurge(true, false);
        SetHealing(true);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.HealPlayerLimited(this, magicNumber);
        GameActions.Bottom.Callback(() ->
        {
            final int amount = player.currentHealth / 2;
            GameActions.Bottom.LoseHPUntilEndOfCombat(amount, AttackEffects.NONE);
            GameActions.Bottom.ApplyPower(new UnnamedDollPlayerPower(player, amount));
        });
    }

    public class UnnamedDollPlayerPower extends AnimatorPower implements InvisiblePower
    {
        private UnnamedDoll_SummoningRitual doll;

        public UnnamedDollPlayerPower(AbstractCreature owner, int amount)
        {
            super(owner, SummoningRitual.DATA);

            this.amount = amount;
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            updateDescription();

            final float x = owner.drawX + (180f * Settings.scale);
            final float y = owner.drawY + (owner.hb_h * 0.5f) + (180f * Settings.scale);

            GameActions.Top.VFX(new HemokinesisEffect2(owner.hb.cX, owner.hb.cY, x, y))
            .AddCallback(new UnnamedDoll_SummoningRitual(x, y), (c, __) ->
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

            if (doll != null && !GameUtilities.IsDeadOrEscaped(doll))
            {
                doll.takeTurn();
            }
        }

        @Override
        public void update(int slot)
        {
            super.update(slot);

            if (doll != null && !GameUtilities.IsDeadOrEscaped(doll))
            {
                doll.update();
                GR.UI.AddPreRender(doll::render);
            }
        }

        @Override
        public int onLoseHp(int damageAmount)
        {
            if (doll != null && !GameUtilities.IsDeadOrEscaped(doll) && damageAmount > 0)
            {
                doll.damage(new DamageInfo(null, damageAmount, DamageInfo.DamageType.HP_LOSS));
                damageAmount = 0;
            }

            return super.onLoseHp(damageAmount);
        }
    }
}