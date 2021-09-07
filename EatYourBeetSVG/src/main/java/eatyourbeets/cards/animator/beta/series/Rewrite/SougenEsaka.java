package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class SougenEsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public SougenEsaka()
    {
        super(DATA);

        Initialize(5, 0, 2, 1);
        SetUpgrade(1, 0, 2);
        SetAffinity_Green(1, 1, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        int agility = GameUtilities.GetPowerAmount(AbstractDungeon.player, AgilityPower.POWER_ID);
        return super.ModifyBlock(enemy, amount + agility);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int agility = GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID);
        if (agility > 0)
        {
            GameActions.Bottom.GainBlock(block);
        }

        if (HasSynergy())
        {
            RandomizedList<AbstractCard> cardsToGainAttack = new RandomizedList<>();

            for (AbstractCard card : player.hand.group)
            {
                if (card.type.equals(CardType.ATTACK) && card.baseDamage > 0)
                {
                    cardsToGainAttack.Add(card);
                }
            }

            AbstractCard card = cardsToGainAttack.Retrieve(rng);

            if (card != null)
            {
                GameUtilities.IncreaseDamage(card, magicNumber, false);
            }
        }
    }
}