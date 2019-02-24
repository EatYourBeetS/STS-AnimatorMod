package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class ItamiYouji extends AnimatorCard
{
    public static final String ID = CreateFullID(ItamiYouji.class.getSimpleName());

    public ItamiYouji()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);

        Initialize(4,0,0);

        this.retain = true;

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        this.magicNumber = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        this.isMagicNumberModified = this.magicNumber > 0;
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();
        this.retain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
            GameActionsHelper.DamageRandomEnemy(p, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}