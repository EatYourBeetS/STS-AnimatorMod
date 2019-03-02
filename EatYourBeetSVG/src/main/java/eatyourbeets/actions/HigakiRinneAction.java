package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.animator.HigakiRinne;
import eatyourbeets.misc.RandomizedList;

import java.util.ArrayList;

public class HigakiRinneAction extends AnimatorAction
{
    private final HigakiRinne higakiRinne;

    public HigakiRinneAction(HigakiRinne higakiRinne)
    {
        this.higakiRinne = higakiRinne;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    public void update()
    {
        //logger.info("Rinne says: " + n);
        AbstractPlayer p = AbstractDungeon.player;
        int n = AbstractDungeon.miscRng.random(100);
        if (n < 6) // 6%
        {
            for (int i = 0; i < 3; i++)
            {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, higakiRinne.block));
            }
        }
        else if (n < 12) // 6%
        {
            for (int i = 0; i < 3; i++)
            {
                AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(p, higakiRinne.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
            }
        }
        else if (n < 18) // 4%
        {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(AbstractOrb.getRandomOrb(true)));
        }
        else if (n < 22) // 6%
        {
            GameActionsHelper.DrawCard(p, 1);
        }
        else if (n < 26) // 8%
        {
            GameActionsHelper.AddToBottom(new UpgradeRandomCardAction());
        }
        else if (n < 30) // 8%
        {
            GameActionsHelper.ApplyPower(p, p, new FocusPower(p, 1), 1);
        }
        else if (n < 35) // 5%
        {
            GameActionsHelper.AddToBottom(new ApplyPoisonOnRandomMonsterAction(p, 5, false, AbstractGameAction.AttackEffect.POISON));
        }
        else if (n < 40) // 5%
        {
            GameActionsHelper.GainEnergy(1);
        }
        else if (n < 45) // 5%
        {
            GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, 1), 1);
        }
        else if (n < 50) // 5%
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
        }
        else if (n < 55) // 5%
        {
            GameActionsHelper.ApplyPower(p, p, new IntangiblePlayerPower(p, 1), 1);
        }
        else if (n < 60) // 5%
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
        }
        else if (n < 65) // 5%
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, 2, false), 2));
        }
        else if (n < 70) // 5%
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, 2, false), 2));
        }
        else if (n < 75) // 5%
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Shiv()));
        }
        else if (n < 80) // 5%
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Madness()));
        }
        else if (n < 85) // 5%
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Slimed()));
        }
        else if (n < 90) // 5%
        {
            AbstractCard card = CardLibrary.getRandomColorSpecificCard(higakiRinne.color, AbstractDungeon.miscRng);
            if (!card.tags.contains(AbstractCard.CardTags.HEALING))
            {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
            }
        }
        else if (n < 95)
        {
            AbstractDungeon.actionManager.addToBottom(new SFXAction(Utilities.GetRandomElement(sounds)));
        }
        else if (n < 97)
        {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "???", 1.0F, 2.0F));
        }
        else if (n < 99)
        {
            ArrayList<String> keys = new ArrayList<>(CardLibrary.cards.keySet());
            String key = Utilities.GetRandomElement(keys, AbstractDungeon.miscRng);
            AbstractCard card = CardLibrary.cards.get(key).makeCopy();
            if (!card.tags.contains(AbstractCard.CardTags.HEALING))
            {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
            }
        }

        this.isDone = true;
    }

    private static final ArrayList<String> sounds = new ArrayList<>();

    static
    {
        sounds.add("VO_AWAKENEDONE_3");
        sounds.add("VO_GIANTHEAD_1B");
        sounds.add("VO_GREMLINANGRY_1A");
        sounds.add("VO_GREMLINCALM_2A");
        sounds.add("VO_GREMLINFAT_2A");
        sounds.add("VO_GREMLINNOB_1B");
        sounds.add("VO_HEALER_1A");
        sounds.add("VO_MERCENARY_1B");
        sounds.add("VO_MERCHANT_MB");
        sounds.add("VO_SLAVERBLUE_2A");
        sounds.add("THUNDERCLAP");
        sounds.add("BELL");
        sounds.add("BELL");
        sounds.add("BELL");
        sounds.add("NECRONOMICON");
        sounds.add("INTIMIDATE");
    }
}
