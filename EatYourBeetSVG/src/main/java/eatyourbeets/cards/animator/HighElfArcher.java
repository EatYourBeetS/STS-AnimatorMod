package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class HighElfArcher extends AnimatorCard
{
    public static final String ID = CreateFullID(HighElfArcher.class.getSimpleName());

    public HighElfArcher()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(5, 0, 1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        //GameActionsHelper.DamageTarget(p, m, this.damage, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int count = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
            if (c instanceof HighElfArcher)
            {
                count += 1;
            }
        }

        if (count <= 1) // cardsPlayedThisTurn contains this card
        {
            GameActionsHelper.DrawCard(p, this.magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}