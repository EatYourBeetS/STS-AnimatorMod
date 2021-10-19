package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MatouSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MatouSakura.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public MatouSakura()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Light();
        SetAffinity_Dark();

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 20);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
       GameActions.Bottom.ChannelOrb(new Dark())
       .AddCallback(orbs ->
       {
           for (AbstractOrb o : orbs)
           {
               GameActions.Bottom.TriggerOrbPassive(o, magicNumber);
           }
       });

        if (CheckAffinity(Affinity.Light))
        {
            for (AbstractCard c : p.hand.group)
            {
                GameUtilities.GainAffinity(c, Affinity.Light);
            }

            for (AbstractCard c : p.drawPile.group)
            {
                GameUtilities.GainAffinity(c, Affinity.Light);
            }
        }
    }
}