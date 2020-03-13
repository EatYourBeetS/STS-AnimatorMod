package eatyourbeets.cards.animator.beta;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SakuraMatou extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(SakuraMatou.class).SetSkill(2, CardRarity.UNCOMMON);

    public SakuraMatou()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        this.magicNumber = this.baseMagicNumber;

        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && Dark.ORB_ID.equals(orb.ID))
            {
                this.magicNumber = orb.evokeAmount / 2;
            }
        }

        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int constricted = 0;
        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && Dark.ORB_ID.equals(orb.ID) && orb.evokeAmount > 0)
            {
                constricted = (orb.evokeAmount /= 2);

                if (orb.evokeAmount == 0)
                {
                    GameActions.Top.Add(new EvokeSpecificOrbAction(orb));
                }
            }
        }

        if (constricted > 0)
        {
            for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.ApplyConstricted(p, enemy, magicNumber);
            }
        }
    }
}