package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.NagisaMomoe_Charlotte;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NagisaMomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NagisaMomoe.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new NagisaMomoe_Charlotte(), true);
            });

    public NagisaMomoe()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Star(1);

        SetEthereal(true);
        SetExhaust(true);
        SetCooldown(0, 0, NagisaMomoe_Charlotte::new);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainRandomAffinityPower(1, upgraded, Affinity.Green, Affinity.Blue, Affinity.Light);
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this) && (GameUtilities.GetAffinityLevel(c, Affinity.Blue, true) >= 2 || GameUtilities.GetAffinityLevel(c, Affinity.Light, true) >= 2))
        {
            GameActions.Bottom.GainTemporaryHP(1);
            GameActions.Bottom.Flash(this);
        }
    }
}