package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.relics.animator.Readme;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.relics.UnnamedReign.UnnamedReignRelic;
import patches.RelicObtainedPatches;
import patches.StoreRelicPatch;

import java.util.ArrayList;

public class Gilgamesh extends AnimatorCard
{
    public static final String ID = Register(Gilgamesh.class.getSimpleName(), EYBCardBadge.Special);
    public static final int GOLD_REWARD = 25;

    private static AbstractRelic lastRelicObtained = null;

    public Gilgamesh()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(3,0, 3, GOLD_REWARD);

        SetUnique(true);
        SetSynergy(Synergies.Fate);
    }

    public static void OnRelicReceived(AbstractRelic relic, RelicObtainedPatches.Trigger trigger)
    {
        if (lastRelicObtained != relic)
        {
            lastRelicObtained = relic;
        }
        else
        {
            return;
        }

        AbstractPlayer player = AbstractDungeon.player;
        if (player != null && player.masterDeck != null
                && !(relic instanceof UnnamedReignRelic)
                && !(relic instanceof Readme))
        {
            for (AbstractRelic r : StoreRelicPatch.last20Relics)
            {
                if (r == relic)
                {
                    return;
                }
            }

            ArrayList<AbstractCard> deck = player.masterDeck.group;
            if (deck != null && deck.size() > 0)
            {
                boolean effectPlayed = false;
                for (AbstractCard c : deck)
                {
                    if (c.cardID.equals(Gilgamesh.ID))
                    {
                        c.upgrade();
                        AbstractDungeon.player.bottledCardUpgradeCheck(c);
                        if (!effectPlayed)
                        {
                            effectPlayed = true;
                            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
                            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
                        }
                        AbstractDungeon.player.gainGold(Gilgamesh.GOLD_REWARD);
                    }
                }
            }
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (timesUpgraded >= 8)
        {
            //GameActionsHelper.SFX("ORB_LIGHTNING_EVOKE", 0.1f);
            GameActionsHelper.VFX(new BorderLongFlashEffect(Color.GOLD));
            GameActionsHelper.SFX("ORB_DARK_EVOKE", 0.1f);

            GameActionsHelper.SFX("ATTACK_WHIRLWIND");
            GameActionsHelper.VFX(new WhirlwindEffect(), 0.0F);

            for (int i = 0; i < this.magicNumber; i++)
            {
                GameActionsHelper.SFX("ATTACK_HEAVY");
                GameActionsHelper.AddToBottom(new VFXAction(p, new CleaveEffect(), 0.0F));
                GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
                GameActionsHelper.VFX(new IronWaveEffect(p.hb.cX, p.hb.cY, m.hb.cX), 0.1F);
            }
        }
        else
        {
            for (int i = 0; i < this.magicNumber; i++)
            {
                GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            }
        }
    }

    @Override
    public void upgrade()
    {
        this.timesUpgraded += 1;
        this.upgradeDamage(1);
        this.upgraded = true;
        this.name = cardData.strings.NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }
}