package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.Hemokinesis2Effect;
import eatyourbeets.utilities.GameActionsHelper_Legacy; import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Shalltear extends AnimatorCard
{
    public static final String ID = Register(Shalltear.class.getSimpleName(), EYBCardBadge.Synergy);

    public Shalltear()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(6,0, 3);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ArrayList<AbstractMonster> enemies = GameUtilities.GetCurrentEnemies(true);
        if (enemies.size() == 0)
        {
            return;
        }

        // Only calculate player powers...
        DamageInfo info = new DamageInfo(p, baseDamage);
        AbstractMonster sample = enemies.get(0);
        ArrayList<AbstractPower> temp = new ArrayList<>(sample.powers);
        sample.powers.clear();
        info.applyPowers(p, sample);
        sample.powers.addAll(temp);
        this.damage = info.output;
        //

        for (AbstractMonster enemy : enemies)
        {
            if (enemies.size() <= 4)
            {
                GameActions.Bottom.VFX(new Hemokinesis2Effect(enemy.hb.cX, enemy.hb.cY, p.hb.cX, p.hb.cY), 0.1f);
            }

            GameActions.Bottom.Add(new LoseHPAction(enemy, p, damage));
            GameActions.Bottom.StealStrength(enemy, 1, false);
        }

        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }
}