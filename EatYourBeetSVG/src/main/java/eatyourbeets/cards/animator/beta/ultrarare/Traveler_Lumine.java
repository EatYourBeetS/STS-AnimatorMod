package eatyourbeets.cards.animator.beta.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DarkOrbEvokeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import eatyourbeets.actions.orbs.WaterOrbEvokeAction;
import eatyourbeets.cards.animator.beta.curse.Curse_AbyssalVoid;
import eatyourbeets.cards.animator.beta.special.Traveler_Wish;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Traveler_Lumine extends AnimatorCard_UltraRare implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Traveler_Lumine.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data -> {
                data.AddPreview(new Traveler_Wish(), false);
                data.AddPreview(new Curse_AbyssalVoid(), false);
            });
    public static final int DEBUFFS_COUNT = 2;

    public Traveler_Lumine()
    {
        super(DATA);

        Initialize(0, 0, 20, 1);
        SetUpgrade(0, 0, 979, 1);
        SetAffinity_Star(1);

        SetEthereal(true);
        SetPurge(true);
        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public int GetXValue() {
        return CombatStats.Affinities.GetAffinityLevel(Affinity.Dark, true) / 2;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Top.Add(new VFXAction(new OfferingEffect(), Settings.FAST_MODE ? 0.1F : 0.5F));
        for (int i = 0; i < secondaryValue; i++) {
            GameActions.Bottom.MakeCardInDrawPile(new Traveler_Wish());
            GameActions.Bottom.MakeCardInDrawPile(new Curse_AbyssalVoid());
        }

        int val = GetXValue();
        if (val > 0) {
            GameActions.Bottom.GainInvocation(val);
        }
        TrySpendAffinity(Affinity.Dark, CombatStats.Affinities.GetAffinityLevel(Affinity.Dark, true));

        Traveler_Lumine other = (Traveler_Lumine) makeStatEquivalentCopy();
        CombatStats.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        int orbCount = CombatStats.OrbsEvokedThisCombat().size();
        if (orbCount > 0)
        {
            GameEffects.Queue.ShowCardBriefly(this);

            GameEffects.Queue.Add(new RoomTintEffect(Color.BLACK.cpy(), 0.8F, 2.0F + (orbCount / 10.0F), true));
            GameEffects.Queue.Add(new BorderLongFlashEffect(new Color(1.0F, 1.0F, 1.0F, 0.5F)));

            int startIdx = Math.max(orbCount - magicNumber, 0);

            for (int i = startIdx; i < orbCount; i++)
            {
                AbstractOrb orb = CombatStats.OrbsEvokedThisCombat().get(i);
                if (Dark.ORB_ID.equals(orb.ID)) {
                    GameActions.Bottom.Add(new DarkOrbEvokeAction(new DamageInfo(AbstractDungeon.player, orb.passiveAmount, DamageInfo.DamageType.THORNS), AttackEffects.DARKNESS));
                }
                else if (Water.ORB_ID.equals(orb.ID)) {
                    GameActions.Bottom.Add(new WaterOrbEvokeAction(orb.hb, orb.passiveAmount));
                }
                else {
                    GameActions.Bottom.InduceOrb(orb,false);
                }
            }

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}