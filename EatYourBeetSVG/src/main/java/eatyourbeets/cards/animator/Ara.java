package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.common.VariableDiscardAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Ara extends AnimatorCard implements MartialArtist
{
    public static final EYBCardBadge[] BADGES = {EYBCardBadge.Discard};
    public static final String ID = Register(Ara.class.getSimpleName(), EYBCardBadge.Special);

    public Ara()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(3,0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + MartialArtist.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        int debuffs = 0;
        for (AbstractPower power : m.powers)
        {
            if (power.type == AbstractPower.PowerType.DEBUFF)
            {
                debuffs += 1;
            }
        }

        GameActionsHelper.DrawCard(p, debuffs);
        GameActionsHelper.AddToBottom(new VariableDiscardAction(this, p, 1, this, this::OnCardDiscard, false));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }

    private void OnCardDiscard(Object state, ArrayList<AbstractCard> cards)
    {
        if (state != this || cards == null || cards.size() == 0)
        {
            return;
        }

        if (cards.get(0).type.equals(CardType.POWER) && PlayerStatistics.TryActivateSemiLimited(cardID))
        {
            GameActionsHelper.GainAgility(1);
            GameActionsHelper.GainForce(2);
        }
    }
}