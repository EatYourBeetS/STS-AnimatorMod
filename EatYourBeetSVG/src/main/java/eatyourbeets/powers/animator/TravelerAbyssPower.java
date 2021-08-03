package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.animator.beta.ultrarare.Traveler;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TravelerAbyssPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(TravelerAbyssPower.class);
    private static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;
    private static final int COUNTDOWN_AMT = 25;


    public TravelerAbyssPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = 0;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, COUNTDOWN_AMT);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        this.amount += 1 + card.cost;
        if (this.amount >= COUNTDOWN_AMT) {
            this.amount -= COUNTDOWN_AMT;
            this.playApplyPowerSfx();
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PURPLE, true));

            GameActions.Bottom.ModifyAllCopies(Traveler.DATA.ID, c -> {
                if (c instanceof Traveler) {
                    Traveler t = (Traveler) c;
                    t.ChangeForm(t.currentForm == Traveler.Form.Aether ? Traveler.Form.Lumine : Traveler.Form.Aether);
                    t.upgrade();
                }
            });

            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.StackPower(owner, new StrengthPower(m, 1));
            }


        }

        this.updateDescription();
    }

    private enum AbyssPositiveEffect {
        ApplyBurning(ACTIONS.ApplyToALL(7, GR.Tooltips.Burning, true), 10, 4),
        ApplyFreezing(ACTIONS.ApplyToALL(30, GR.Tooltips.Freezing, true), 10, 4),
        ApplyVulnerable(ACTIONS.ApplyToALL(2, GR.Tooltips.Vulnerable, true), 10, 4),
        ApplyWeak(ACTIONS.ApplyToALL(2, GR.Tooltips.Weak, true), 10, 4),
        ChannelRandomOrbs(ACTIONS.ChannelRandomOrbs(2, true), 10, 2),
        DrawCards(ACTIONS.Draw(2, true), 10, 2),
        GainElementalMastery(ACTIONS.GainAmount(2, GR.Tooltips.ElementalMastery, true), 8, 2),
        GainIntellect(ACTIONS.GainAmount(2, GR.Tooltips.Intellect, true), 8, 2);


        private final String text;
        private final int weight;
        private final int number;

        AbyssPositiveEffect(String text, int weight, int number)
        {
            this.text = text;
            this.weight = weight;
            this.number = number;
        }
    }
}
