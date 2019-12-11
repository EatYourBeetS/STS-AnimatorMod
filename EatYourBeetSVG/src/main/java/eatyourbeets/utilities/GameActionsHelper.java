package eatyourbeets.utilities;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.common.TemporaryBiasPower;

@SuppressWarnings("UnusedReturnValue")
public class GameActionsHelper
{
    public static void ApplyTemporaryDexterity(AbstractCreature source, AbstractCreature target, int amount)
    {
        if (GameUtilities.UseArtifact(target))
        {
            GameActions.Top.ApplyPowerSilently(source, target, new LoseDexterityPower(target, amount), amount);
        }

        GameActions.Top.ApplyPower(source, target, new DexterityPower(target, amount), amount);
    }

    public static void ApplyTemporaryFocus(AbstractCreature source, AbstractCreature target, int amount)
    {
        if (GameUtilities.UseArtifact(target))
        {
            GameActions.Top.ApplyPowerSilently(source, target, new TemporaryBiasPower(target, amount), amount);
        }

        GameActions.Top.ApplyPower(source, target, new FocusPower(target, amount), amount);
    }

    public static void ApplyTemporaryStrength(AbstractCreature source, AbstractCreature target, int amount)
    {
        if (GameUtilities.UseArtifact(target))
        {
            GameActions.Top.ApplyPowerSilently(source, target, new LoseStrengthPower(target, amount), amount);
        }

        GameActions.Top.ApplyPower(source, target, new StrengthPower(target, amount), amount);
    }

    public static void ClearPostCombatActions()
    {
        AbstractDungeon.actionManager.clearPostCombatActions();
    }

    public static void PlayCard(AbstractCard card, AbstractMonster m)
    {
        if (card.cost > 0)
        {
            card.freeToPlayOnce = true;
        }

        AbstractDungeon.player.limbo.group.add(card);

        card.current_y = -200.0F * Settings.scale;
        card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
        card.target_y = (float) Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;

        if (!card.canUse(AbstractDungeon.player, m))
        {
            GameActions.Top.Add(new UnlimboAction(card));
            GameActions.Top.Add(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
            GameActions.Top.Add(new WaitAction(0.4F));
        }
        else
        {
            card.applyPowers();
            card.freeToPlayOnce = true;

            GameActions.Top.Add(new QueueCardAction(card, m));
            GameActions.Top.Add(new UnlimboAction(card));

            if (!Settings.FAST_MODE)
            {
                GameActions.Top.Add(new WaitAction(Settings.ACTION_DUR_MED));
            }
            else
            {
                GameActions.Top.Add(new WaitAction(Settings.ACTION_DUR_FASTER));
            }
        }
    }

    public static CardQueueItem PlayCopy(AbstractCard source, AbstractMonster m, boolean applyPowers)
    {
        AbstractCard temp = source.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(temp);
        temp.current_x = source.current_x;
        temp.current_y = source.current_y;
        temp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        temp.target_y = (float) Settings.HEIGHT / 2.0F;

        if (temp.cost > 0)
        {
            temp.freeToPlayOnce = true;
        }

        if (applyPowers)
        {
            temp.applyPowers();
        }

        temp.calculateCardDamage(m);
        temp.purgeOnUse = true;

        CardQueueItem item = new CardQueueItem(temp, m, source.energyOnUse, true);
        AbstractDungeon.actionManager.cardQueue.add(item);

        return item;
    }
}
