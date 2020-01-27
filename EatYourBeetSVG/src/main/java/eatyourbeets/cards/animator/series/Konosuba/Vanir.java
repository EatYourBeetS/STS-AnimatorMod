package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Transmogrifier;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Vanir extends AnimatorCard
{
    public static final String ID = Register(Vanir.class, EYBCardBadge.Exhaust);

    public Vanir()
    {
        super(ID, 1, CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(12, 0, 3);
        SetUpgrade(1, 0, -1);

        SetSynergy(Synergies.Konosuba, true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.SelectFromPile(name, 1, AbstractDungeon.player.drawPile)
        .SetOptions(false, true)
        .SetMessage(Transmogrifier.OPTIONS[2])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                ArrayList<AbstractCard> temp = AbstractDungeon.player.drawPile.group;

                AbstractCard card = cards.get(0);
                for (int i = 0; i < temp.size(); i++)
                {
                    if (temp.get(i) == card)
                    {
                        AbstractCard vanir = makeCopy();

                        if (upgraded)
                        {
                            vanir.upgrade();
                        }

                        temp.remove(i);
                        temp.add(i, vanir);

                        return;
                    }
                }
            }
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SMASH);
        GameActions.Bottom.ModifyAllCombatInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
    }
}