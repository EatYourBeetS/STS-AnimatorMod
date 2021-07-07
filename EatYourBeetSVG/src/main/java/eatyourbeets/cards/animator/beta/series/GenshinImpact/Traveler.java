package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FadingPower;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.vfx.SnowballEffect;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.ChilledPower;
import eatyourbeets.powers.animator.SheerColdPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Traveler extends AnimatorCard_UltraRare implements OnStartOfTurnPostDrawSubscriber {
    public static final EYBCardData DATA = Register(Traveler.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Traveler() {
        super(DATA);

        Initialize(0, 0, 3, 10);
        SetUpgrade(0, 0, 0, 30);

        SetEthereal(true);
        SetShapeshifter();
        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (player.maxOrbs > 0) {
            GameActions.Bottom.Add(new DecreaseMaxOrbAction(player.maxOrbs));
        }

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);

        GameActions.Bottom.ChannelOrbs(Aether::new, magicNumber);

        if (CombatStats.OrbsEvokedThisCombat().size() > 0) {
            Traveler other = (Traveler) makeStatEquivalentCopy();
            CombatStats.onStartOfTurnPostDraw.Subscribe(other);
        }

    }

    @Override
    public void OnStartOfTurnPostDraw() {
        int orbCount = CombatStats.OrbsEvokedThisCombat().size();
        if (orbCount > 0) {
            GameEffects.Queue.ShowCardBriefly(this);

            int startIdx = Math.max(orbCount - secondaryValue,0);

            for (int i = startIdx; i < orbCount; i++) {
                AbstractOrb orb = CombatStats.OrbsEvokedThisCombat().get(i);
                orb.onEndOfTurn();
                if (Frost.ORB_ID.equals(orb.ID) && player.hasPower(SheerColdPower.POWER_ID)) {
                    AbstractPower frostPower = player.getPower(FadingPower.POWER_ID);
                    AbstractCreature enemy = GameUtilities.GetRandomEnemy(true);
                    GameActions.Bottom.Add(new ApplyPowerAction(enemy, player, new ChilledPower(enemy, frostPower.amount), frostPower.amount, true));
                    GameActions.Top.Wait(0.15f);
                    GameActions.Top.VFX(new SnowballEffect(orb.hb.cX, orb.hb.cY, enemy.hb.cX, enemy.hb.cY)
                            .SetColor(Color.SKY, Color.CYAN).SetRealtime(true));
                }
                else if (Dark.ORB_ID.equals(orb.ID)) {
                    AbstractOrb weakerOrb = new Dark();
                    weakerOrb.onEvoke();
                }
            }

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}