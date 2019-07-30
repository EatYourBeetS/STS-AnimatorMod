package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.actions.common.ShuffleRandomGoblinAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.powers.animator.DexterityTrainingPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class Spearman extends AnimatorCard
{
    public static final String ID = CreateFullID(Spearman.class.getSimpleName());

    public Spearman()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + player.currentBlock);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTargetPiercing(p, m, damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper.AddToBottom(new LoseBlockAction(p, p, p.currentBlock));

        if (upgraded)
        {
            GameActionsHelper.GainBlock(p, block);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeBlock(3);
        }
    }
}