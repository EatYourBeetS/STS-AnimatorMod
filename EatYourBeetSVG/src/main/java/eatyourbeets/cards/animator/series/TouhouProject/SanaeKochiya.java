package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SanaeKochiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SanaeKochiya.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.General), false));

    public SanaeKochiya()
    {
        super(DATA);

        Initialize(0, 2, 1, 5);
        SetUpgrade(0, 2);

        SetAffinity_Light(1, 1, 2);
        SetAffinity_Green(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Top.Discard(this, player.hand)
            .AddCallback(() -> GameActions.Top.ObtainAffinityToken(Affinity.General, false));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlessing(magicNumber, false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() ->
        {
            if (CheckSpecialCondition(true))
            {
                GameActions.Bottom.ChannelOrb(new Aether());
                GameActions.Bottom.Exhaust(this);
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameUtilities.GetPowerAmount(Affinity.Light) >= (tryUse ? secondaryValue : (secondaryValue - magicNumber));
    }
}

