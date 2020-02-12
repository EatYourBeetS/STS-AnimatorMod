package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.utilities.GameActions;

public class Kazuma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kazuma.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Kazuma()
    {
        super(DATA);

        Initialize(4, 6, 4);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (HasSynergy())
        {
            return DamageAttribute.Instance.SetCard(this).SetIconTag(EYBCardTarget.Random.tag);
        }

        return null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.Cycle(name, 1);

        if (HasSynergy())
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
    }
}