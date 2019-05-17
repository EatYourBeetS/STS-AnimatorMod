package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class RoryMercury extends AnimatorCard
{
    public static final String ID = CreateFullID(RoryMercury.class.getSimpleName());

    public RoryMercury()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(5,0, 1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        AbstractPlayer p = AbstractDungeon.player;
        GameActionsHelper.ApplyPowerSilently(p, p, new LoseStrengthPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageRandomEnemy(p, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.DamageRandomEnemy(p, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
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