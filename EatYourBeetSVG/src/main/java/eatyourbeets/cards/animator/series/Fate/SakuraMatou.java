package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SakuraMatou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SakuraMatou.class).SetSkill(1, CardRarity.UNCOMMON);

    public SakuraMatou()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetEthereal(true);
        SetExhaust(true);
        SetSynergy(Synergies.Fate);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        int bonus = 0;
        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && Dark.ORB_ID.equals(orb.ID) && orb.evokeAmount > 0)
            {
                bonus += Math.max(1, orb.evokeAmount / 2);
                break;
            }
        }

        GameUtilities.IncreaseMagicNumber(this, bonus, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyConstricted(p, m, magicNumber);
        GameActions.Top.Callback(() ->
        {
            AbstractOrb toEvoke = null;
            for (AbstractOrb orb : player.orbs)
            {
                if (orb != null && Dark.ORB_ID.equals(orb.ID) && orb.evokeAmount > 0)
                {
                    if ((orb.evokeAmount /= 2) == 0)
                    {
                        toEvoke = orb;
                    }

                    break;
                }
            }

            if (toEvoke != null)
            {
                player.orbs.remove(toEvoke);
                player.orbs.add(0, toEvoke);
                player.evokeOrb();
            }
        });
    }
}