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
import eatyourbeets.cards.animator.beta.special.Traveler_Wish;
import eatyourbeets.cards.animator.beta.status.AbyssalVoid;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Traveler_Lumine extends AnimatorCard_UltraRare implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Traveler_Lumine.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);
    static {
        DATA.AddPreview(new Traveler_Wish(), true);
        DATA.AddPreview(new AbyssalVoid(), true);
    }

    public Traveler_Lumine()
    {
        super(DATA);

        Initialize(0, 0, 20, 2);
        SetUpgrade(0, 0, 979, 0);
        SetAffinity_Light(2);
        SetAffinity_Blue(1);
        SetEthereal(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Top.Add(new VFXAction(new OfferingEffect(), Settings.FAST_MODE ? 0.1F : 0.5F));
        GameActions.Bottom.MakeCardInDrawPile(new AbyssalVoid());
        GameActions.Bottom.MakeCardInDrawPile(new Traveler_Wish());
        GameActions.Bottom.MakeCardInDiscardPile(new AbyssalVoid());
        GameActions.Bottom.MakeCardInDiscardPile(new Traveler_Wish());

        if (GameUtilities.GetDebuffsCount(player.powers) == secondaryValue && CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.GainIntellect(GetHandAffinity(Affinity.Dark), upgraded);
        }

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

            AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.8F, 2.0F + (orbCount / 10.0F), true));
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(1.0F, 1.0F, 1.0F, 0.5F)));

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
                    GameActions.Bottom.TriggerOrbPassive(orb, 1);
                }
            }

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}