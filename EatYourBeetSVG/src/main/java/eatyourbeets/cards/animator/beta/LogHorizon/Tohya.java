package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Tohya extends AnimatorCard {
    public static final EYBCardData DATA = Register(Tohya.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);

    static
    {
        DATA.AddPreview(new Minori(), false);
    }

    public Tohya() {
        super(DATA);

        Initialize(5, 0, 1, 0);
        SetUpgrade(3, 0, 0, 0);

        SetMartialArtist();

        SetSynergy(Synergies.LogHorizon);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        GameActions.Bottom.Callback(() ->
        {
            DrawMinori(player.drawPile);
        });

        if (IsStarter())
        {
            GameActions.Bottom.GainBlur(magicNumber);
        }
    }

    private boolean DrawMinori(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Minori.DATA.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    GameActions.Top.MoveCard(c, group, player.hand)
                            .ShowEffect(true, true)
                            .AddCallback(GameUtilities::Retain);
                }

                return true;
            }
        }

        return false;
    }
}