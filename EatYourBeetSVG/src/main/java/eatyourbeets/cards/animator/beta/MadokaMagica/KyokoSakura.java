package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KyokoSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyokoSakura.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);

    public KyokoSakura()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(1, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.Cycle(name, magicNumber).AddCallback(m, (enemy, cards) ->
        {
            if (cards.size() > 0)
            {
                for (AbstractCard card : cards)
                {
                    if (GameUtilities.IsCurseOrStatus(card))
                    {
                        GameActions.Bottom.ChannelOrb(new Fire(), true);
                        return;
                    }
                }
            }
        });
    }
}
