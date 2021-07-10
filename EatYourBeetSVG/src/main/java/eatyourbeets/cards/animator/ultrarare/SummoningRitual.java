package eatyourbeets.cards.animator.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.VFX;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.vfx.HemokinesisEffect;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll_Player;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class SummoningRitual extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SummoningRitual.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public SummoningRitual()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, -1);

        SetRetain(true);
        SetPurge(true, false);

        SetAffinity_Dark(2);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ApplyPower(new UnnamedDollPlayerPower(p, cardData));
    }

    public class UnnamedDollPlayerPower extends AnimatorPower
    {
        private TheUnnamed_Doll_Player doll;

        public UnnamedDollPlayerPower(AbstractCreature owner, EYBCardData data)
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

            GameActions.Bottom.Callback(new VFX(new HemokinesisEffect(owner.hb.cX, owner.hb.cY, x, y), false)
            .SetDuration(0.5f, true))
            .AddCallback(new TheUnnamed_Doll_Player(x, y), (c, __) ->
            {
                this.doll = c;
                this.doll.rollMove();
            });
            GameActions.Bottom.LoseHP(amount, AbstractGameAction.AttackEffect.NONE);
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
            }
        }

        @Override
        public void renderIcons(SpriteBatch sb, float x, float y, Color c)
        {
            super.renderIcons(sb, x, y, c);

            if (doll != null)
            {
                doll.render(sb);
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
    }
}