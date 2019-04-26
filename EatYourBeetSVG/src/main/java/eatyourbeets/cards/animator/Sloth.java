package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Sloth extends AnimatorCard
{
    public static final String ID = CreateFullID(Sloth.class.getSimpleName());

    public Sloth()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(2,0, 2);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        int count = 0;
        ArrayList<String> orbs = new ArrayList<>();
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
        {
            if (!(orb instanceof EmptyOrbSlot) && !orbs.contains(orb.ID))
            {
                orbs.add(orb.ID);
                count += 1;
            }
        }

        if (count > 0)
        {
            GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, count * this.magicNumber));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
            upgradeMagicNumber(1);
        }
    }
}