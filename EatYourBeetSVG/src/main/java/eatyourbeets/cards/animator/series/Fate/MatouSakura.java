package eatyourbeets.cards.animator.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.actions.orbs.RemoveOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class MatouSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MatouSakura.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public MatouSakura()
    {
        super(DATA);

        Initialize(0, 2, 2, 4);
        SetUpgrade(0, 2, 1, 0);

        SetAffinity_Dark(2, 0, 2);
        SetAffinity_Blue(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 4);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(GameUtilities.HasOrb(Dark.ORB_ID) ? EYBCardTarget.Normal : EYBCardTarget.None);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Callback(m, (enemy, __) ->
        {
           final AbstractOrb orb = GameUtilities.GetFirstOrb(Dark.ORB_ID);
           if (orb != null)
           {
               GameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
               GameEffects.Queue.BorderLongFlash(new Color(1.0F, 0.0F, 1.0F, 0.7F));
               GameEffects.Queue.Attack(player, enemy, AttackEffects.DARK, 1.1f, 1.2f);
               GameActions.Bottom.Add(new RemoveOrb(orb));
               GameActions.Bottom.ApplyConstricted(player, enemy, orb.evokeAmount / 2);
               GameActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.85f, 0.9f);
           }
           else
           {
               GameActions.Bottom.ChannelOrb(new Dark())
               .AddCallback(orbs ->
               {
                   for (AbstractOrb o : orbs)
                   {
                       GameActions.Bottom.TriggerOrbPassive(o, magicNumber);
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