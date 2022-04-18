package eatyourbeets.cards.animator.curse.common;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Normality extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Normality.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, false);

    public Curse_Normality()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Last.ApplyPower(new Curse_NormalityPower(player))
        .ShowEffect(false, true)
        .IgnoreArtifact(true)
        .AddCallback(p ->
        {
            if (p != null)
            {
                GameActions.Top.Flash(this);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    public static class Curse_NormalityPower extends AnimatorPower implements InvisiblePower
    {
        public Curse_NormalityPower(AbstractCreature owner)
        {
            super(owner, Curse_Normality.DATA);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            if (!owner.hasPower(NoDrawPower.POWER_ID))
            {
                GameUtilities.ApplyPowerInstantly(owner, new NoDrawPower(owner), -1).name += GetName();
            }
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            final AbstractPower power = GameUtilities.GetPower(owner, NoDrawPower.POWER_ID);
            if (power != null && power.name.endsWith(GetName()))
            {
                owner.powers.remove(power);
            }
        }

        @Override
        public void updateDescription()
        {
            super.updateDescription();

            if (AbstractDungeon.actionManager.currentAction == null)
            {
                Refresh();
            }
        }

        @Override
        public void onAfterUseCard(AbstractCard card, UseCardAction action)
        {
            super.onAfterUseCard(card, action);

            GameActions.Bottom.Callback(this::Refresh);
        }

        private void Refresh()
        {
            if (enabled)
            {
                for (AbstractCard c : player.hand.group)
                {
                    if (Curse_Normality.DATA.IsCard(c))
                    {
                        return;
                    }
                }

                SetEnabled(false);
                RemovePower();
            }
            else
            {
                RemovePower();
            }
        }

        private static String GetName()
        {
            return " (" + Curse_Normality.DATA.Strings.NAME + ")";
        }
    }
}