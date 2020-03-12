package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class TohkaYatogami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TohkaYatogami.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal);
    public static final int DAMAGE_AMOUNT = 10;
    static
    {
        DATA.AddPreview(new InverseTohka(), true);
    }

    public TohkaYatogami()
    {
        super(DATA);

        Initialize(DAMAGE_AMOUNT, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return DAMAGE_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        if (p.exhaustPile.size() >= 10)
        {
            GameActions.Last.ReplaceCard(this.uuid, new InverseTohka()).SetUpgrade(upgraded);
        }
    }
}