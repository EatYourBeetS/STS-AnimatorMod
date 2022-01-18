package pinacolada.cards.pcl.ultrarare;

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
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import pinacolada.actions.orbs.WaterOrbEvokeAction;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.curse.Curse_AbyssalVoid;
import pinacolada.cards.pcl.special.Traveler_Wish;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Water;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Traveler_Lumine extends PCLCard_UltraRare implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Traveler_Lumine.class)
            .SetSkill(1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
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
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public int GetXValue() {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Dark, true) / 2;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Top.Add(new VFXAction(new OfferingEffect(), Settings.FAST_MODE ? 0.1F : 0.5F));
        for (int i = 0; i < secondaryValue; i++) {
            PCLActions.Bottom.MakeCardInDrawPile(new Traveler_Wish());
            PCLActions.Bottom.MakeCardInDrawPile(new Curse_AbyssalVoid());
        }

        int val = GetXValue();
        if (val > 0) {
            PCLActions.Bottom.GainInvocation(val);
        }
        TrySpendAffinity(PCLAffinity.Dark, PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Dark, true));

        Traveler_Lumine other = (Traveler_Lumine) makeStatEquivalentCopy();
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        int orbCount = CombatStats.OrbsEvokedThisCombat().size();
        if (orbCount > 0)
        {
            PCLGameEffects.Queue.ShowCardBriefly(this);

            PCLGameEffects.Queue.Add(new RoomTintEffect(Color.BLACK.cpy(), 0.8F, 2.0F + (orbCount / 10.0F), true));
            PCLGameEffects.Queue.Add(new BorderLongFlashEffect(new Color(1.0F, 1.0F, 1.0F, 0.5F)));

            int startIdx = Math.max(orbCount - magicNumber, 0);

            for (int i = startIdx; i < orbCount; i++)
            {
                AbstractOrb orb = CombatStats.OrbsEvokedThisCombat().get(i);
                if (Dark.ORB_ID.equals(orb.ID)) {
                    PCLActions.Bottom.Add(new DarkOrbEvokeAction(new DamageInfo(AbstractDungeon.player, orb.passiveAmount, DamageInfo.DamageType.THORNS), AttackEffects.DARKNESS));
                }
                else if (Water.ORB_ID.equals(orb.ID)) {
                    PCLActions.Bottom.Add(new WaterOrbEvokeAction(orb.hb, orb.passiveAmount));
                }
                else {
                    PCLActions.Bottom.InduceOrb(orb,false);
                }
            }

            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}