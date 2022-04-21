package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class MatouSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MatouSakura.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public MatouSakura()
    {
        super(DATA);

        Initialize(0, 3, 3);
        SetUpgrade(0, 0, 2);

        SetAffinity_Dark(1, 1, 1);
        SetAffinity_Blue(1, 0, 1);

        SetEvokeOrbCount(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.TriggerOrbPassive(1)
        .SetFilter(o -> Dark.ORB_ID.equals(o.ID))
        .AddCallback(orbs ->
        {
            if (orbs.size() > 0)
            {
                GameActions.Bottom.GainBlock(magicNumber);
                GameActions.Bottom.Flash(this);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainCorruption(1, upgraded);
        GameActions.Bottom.ChannelOrb(new Dark());

//        GameActions.Bottom.Callback(m, (enemy, __) ->
//        {
//           final AbstractOrb orb = GameUtilities.GetFirstOrb(Dark.ORB_ID);
//           if (orb != null)
//           {
//               GameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
//               GameEffects.Queue.BorderLongFlash(new Color(1.0F, 0.0F, 1.0F, 0.7F));
//               GameEffects.Queue.Attack(player, enemy, AttackEffects.DARK, 1.1f, 1.2f);
//               GameActions.Bottom.Add(new RemoveOrb(orb));
//               GameActions.Bottom.ApplyConstricted(player, enemy, orb.evokeAmount / 2);
//               GameActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.85f, 0.9f);
//           }
//           else
//           {
//               GameActions.Bottom.ChannelOrb(new Dark())
//               .AddCallback(orbs ->
//               {
//                   for (AbstractOrb o : orbs)
//                   {
//                       GameActions.Bottom.TriggerOrbPassive(o, magicNumber);
//                   }
//               });
//           }
//        });
    }
}