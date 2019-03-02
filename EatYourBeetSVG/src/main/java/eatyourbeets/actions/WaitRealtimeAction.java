package eatyourbeets.actions;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.CorruptionPower;
import eatyourbeets.cards.animator.Wiz;

public class WaitRealtimeAction extends AbstractGameAction
{
    public WaitRealtimeAction(float setDur)
    {
        this.setValues(null, null, 0);

        this.duration = setDur;

        this.actionType = ActionType.WAIT;
    }

    public void update()
    {
        this.tickDuration();
    }
}
