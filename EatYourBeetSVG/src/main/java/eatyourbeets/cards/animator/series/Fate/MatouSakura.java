package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.actions.orbs.RemoveOrb;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class MatouSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MatouSakura.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public MatouSakura()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Dark(2);
        SetAffinity_Blue(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 5);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(GameUtilities.HasOrb(Dark.ORB_ID) ? EYBCardTarget.Normal : EYBCardTarget.None);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Callback(m, (enemy, __) ->
        {
           final AbstractOrb best = JUtils.FindMax(JUtils.Filter(player.orbs, Dark.class::isInstance), o -> o.evokeAmount);
           if (best != null)
           {
               GameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
               GameEffects.Queue.BorderLongFlash(new Color(1.0F, 0.0F, 1.0F, 0.7F));
               GameEffects.Queue.Attack(player, enemy, AttackEffects.DARK, 1.1f, 1.2f);
               GameActions.Bottom.Add(new RemoveOrb(best));
               GameActions.Bottom.ApplyConstricted(player, enemy, best.evokeAmount / 2);
               GameActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.85f, 0.9f);
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

        if (CheckAffinity(Affinity.Light))
        {
            GameActions.Bottom.GainBlessing(secondaryValue);
        }
    }
}