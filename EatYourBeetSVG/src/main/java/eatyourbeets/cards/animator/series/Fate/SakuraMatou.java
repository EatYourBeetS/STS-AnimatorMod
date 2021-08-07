package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateParticle;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import eatyourbeets.actions.orbs.RemoveOrb;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.vfx.megacritCopy.OrbFlareEffect2;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class SakuraMatou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SakuraMatou.class)
            .SetSkill(2, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public SakuraMatou()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Dark(2);
        SetAffinity_Blue(1);

        SetExhaust(true);

        SetAffinityRequirement(AffinityType.Light, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Callback(m, (enemy, __) ->
        {
           final AbstractOrb best = JUtils.FindMax(JUtils.Filter(player.orbs, Dark.class::isInstance), o -> o.evokeAmount);
           if (best != null)
           {
               SFX.Play(SFX.ORB_DARK_EVOKE, 1.1f, 1.2f);
               GameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
               GameEffects.Queue.BorderLongFlash(new Color(1.0F, 0.0F, 1.0F, 0.7F));
               GameEffects.TopLevelQueue.Add(new OrbFlareEffect2(enemy.hb.cX, enemy.hb.cY)).SetColors(OrbFlareEffect.OrbFlareColor.DARK).renderBehind = false;
               for (int i = 0; i < 4; i++)
               {
                   GameEffects.TopLevelQueue.Add(new DarkOrbActivateParticle(enemy.hb.cX, enemy.hb.cY)).renderBehind = false;
               }
               GameActions.Bottom.Add(new RemoveOrb(best));
               GameActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.85f, 0.9f);
               GameActions.Bottom.ApplyConstricted(player, enemy, best.evokeAmount / 2);
           }
           else
           {
               GameActions.Bottom.ChannelOrb(new Dark())
               .AddCallback(orbs ->
               {
                   for (AbstractOrb orb : orbs)
                   {
                       GameActions.Bottom.TriggerOrbPassive(orb, magicNumber);
                   }
               });
           }
        });

        if (CheckAffinity(AffinityType.Light))
        {
            GameActions.Bottom.GainBlessing(secondaryValue);
        }
    }
}