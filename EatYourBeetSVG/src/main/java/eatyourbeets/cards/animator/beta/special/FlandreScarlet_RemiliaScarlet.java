package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class FlandreScarlet_RemiliaScarlet extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FlandreScarlet_RemiliaScarlet.class).SetAttack(1, CardRarity.SPECIAL).SetSeries(CardSeries.TouhouProject);

    public FlandreScarlet_RemiliaScarlet()
    {
        super(DATA);

        Initialize(8, 0, 3, 2);
        SetUpgrade(1, 0, 0, 1);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(2);


        SetEthereal(true);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 6);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + magicNumber * JUtils.Count(player.hand.group, c -> c.type == CardType.ATTACK));
    }

    public AbstractAttribute GetSpecialInfo() {
        return HPAttribute.Instance.SetCard(this);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null) {
            CalculateHeal();
        }
        else {
            heal = 0;
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BITE);
        if (CalculateHeal() > 0)
        {
            GameActions.Bottom.RecoverHP(heal);
        }
        boolean isTemporary = TrySpendAffinity(Affinity.Light);
        GameActions.Delayed.SelectFromHand(name, player.hand.size() - 1, true).SetFilter(c -> c.uuid != this.uuid && c.type == CardType.ATTACK).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                GameUtilities.ModifyDamage(c, magicNumber, isTemporary);
            }
        });
    }

    protected int CalculateHeal()
    {
        return heal = Math.min(damage, GameActionManager.playerHpLastTurn - player.currentHealth);
    }
}

