package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.subscribers.OnRemoveFromDeckSubscriber;

public class Strike_Kancolle extends Strike implements OnRemoveFromDeckSubscriber
{
    public static final String ID = CreateFullID(Strike_Kancolle.class.getSimpleName());

    public Strike_Kancolle()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6,0, 40);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeMagicNumber(20);
        }
    }

    @Override
    public void OnRemoveFromDeck()
    {
        AbstractDungeon.player.gainGold(this.magicNumber);
    }
}