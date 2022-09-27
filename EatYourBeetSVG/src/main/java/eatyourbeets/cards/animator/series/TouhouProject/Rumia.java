package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rumia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rumia.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Dark), true));

    public Rumia()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Dark(1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.TryActivateStarter())
        {
            GameActions.Bottom.Motivate().SetFilter(GameUtilities::HasDarkAffinity);
        }

        GameActions.Bottom.TriggerOrbPassive(1)
        .SetFilter(o -> Dark.ORB_ID.equals(o.ID))
        .AddCallback(orbs ->
        {
            if (orbs.isEmpty())
            {
                GameActions.Top.ChannelOrb(new Dark());
            }
            else
            {
                GameActions.Top.ObtainAffinityToken(Affinity.Dark, upgraded);
                GameActions.Top.SFX(SFX.ATTACK_MAGIC_FAST_1, 0.75f, 0.8f).SetDuration(0.2f, true);
            }
        });
    }
}