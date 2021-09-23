package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Father extends AnimatorCard {
    private static final AbstractRelic relic = new PhilosopherStone();
    private static final EYBCardTooltip tooltip;
    public static final EYBCardData DATA;

    public Father() {
        super(DATA);
        this.Initialize(0, 0, 3, 45);
        this.SetCostUpgrade(-1);
        this.SetAffinity_Dark(2);
        this.SetExhaust(true);
        this.SetObtainableInCombat(false);
    }

    public void initializeDescription() {
        super.initializeDescription();
        if (this.cardText != null) {
            tooltip.SetIcon(relic);
            tooltip.id = this.cardID + ":" + tooltip.title;
            this.tooltips.add(tooltip);
        }

    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        if (!p.hasRelic(relic.relicId)) {
            p.decreaseMaxHealth((int)Math.ceil((double)((float)p.maxHealth * ((float)this.secondaryValue / 100.0F))));
            GameActions.Bottom.VFX(new OfferingEffect(), 0.5F);
            GameActions.Bottom.Callback(() -> {
                GameEffects.Queue.SpawnRelic(relic.makeCopy(), this.current_x, this.current_y);
            });
            AbstractDungeon.bossRelicPool.remove(relic.relicId);
            ++p.energy.energy;
        } else {
            GameActions.Bottom.GainVitality(this.magicNumber);
        }

    }

    static {
        tooltip = new EYBCardTooltip(relic.name, relic.description);
        DATA = Register(Father.class).SetSkill(4, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(1).SetSeriesFromClassPackage();
    }
}