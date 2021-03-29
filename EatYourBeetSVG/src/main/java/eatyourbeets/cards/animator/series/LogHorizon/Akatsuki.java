package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Akatsuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Akatsuki.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing);

    public Akatsuki()
    {
        super(DATA);

        Initialize(10, 0, 2);
        SetUpgrade(2, 0, 1);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return (other.rarity.equals(CardRarity.UNCOMMON)) || super.HasDirectSynergy(other);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Top.MakeCardInHand(this)
                .SetUpgrade(false, true)
                .AddCallback(card ->
                {
                    GameUtilities.ModifyCostForCombat(card, 0, false);
                    GameUtilities.ModifyDamage(card, 0, false);
                    card.exhaust = true;
                });
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
        .SetDamageEffect(c -> GameEffects.List.Add(new DieDieDieEffect()));
    }
}