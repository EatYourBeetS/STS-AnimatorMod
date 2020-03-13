package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;


public class SakuraMatou extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(SakuraMatou.class).SetSkill(2, CardRarity.UNCOMMON);
    private static ArrayList<Dark> darkOrbs = new ArrayList<>();

    public SakuraMatou()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
        {
            Dark dark = JavaUtilities.SafeCast(orb, Dark.class);
            if (dark != null)
            {
                darkOrbs.add(dark);
                this.magicNumber = darkOrbs.get(0).evokeAmount / 2;
                break;
            }
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!darkOrbs.isEmpty()){
            darkOrbs.get(0).evokeAmount =- magicNumber;
            for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true)) {
                GameActions.Bottom.ApplyConstricted(p, enemy, magicNumber);
            }
        }
    }
}