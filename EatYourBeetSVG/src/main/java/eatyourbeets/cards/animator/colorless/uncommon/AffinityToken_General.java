package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class AffinityToken_General extends AffinityToken implements OnAddToDeckListener
{
    public static final EYBCardData DATA = Register(AffinityToken_General.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .PostInitialize(data ->
            {
                for (EYBCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), true);
                }
            });
    public static final Affinity AFFINITY_TYPE = Affinity.General;

    public AffinityToken_General()
    {
        super(DATA, AFFINITY_TYPE);

        SetObtainableInCombat(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        final CardGroup group = AffinityToken.CreateTokenGroup(AffinityToken.cards.size(), rng);
        GameEffects.TopLevelQueue.Callback(new SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(m, (enemy, cards) ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Bottom.PlayCopy(c, enemy);
            }
        }));
    }

    @Override
    public boolean OnAddToDeck()
    {
        final CardGroup group = AffinityToken.CreateTokenGroup(AffinityToken.cards.size(), rng);
        GameEffects.TopLevelQueue.Callback(new SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                if (upgraded)
                {
                    c.upgrade();
                }

                GameEffects.TopLevelQueue.ShowAndObtain(c, InputHelper.mX, InputHelper.mY, false);
            }
        }));

        return false;
    }
}