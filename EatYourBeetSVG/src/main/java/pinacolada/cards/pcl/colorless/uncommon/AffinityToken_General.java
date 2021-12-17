package pinacolada.cards.pcl.colorless.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class AffinityToken_General extends AffinityToken implements OnAddToDeckListener
{
    public static final PCLCardData DATA = Register(AffinityToken_General.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .PostInitialize(data ->
            {
                for (PCLCardData d : AffinityToken.GetCards())
                {
                    data.AddPreview(d.CreateNewInstance(), true);
                }
            });
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.General;

    public AffinityToken_General()
    {
        super(DATA, AFFINITY_TYPE);

        SetObtainableInCombat(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final CardGroup group = AffinityToken.CreateTokenGroup(AffinityToken.cards.size(), rng, upgraded);
        PCLGameEffects.TopLevelQueue.Callback(new SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(m, (enemy, cards) ->
        {
            for (AbstractCard c : cards)
            {
                PCLActions.Bottom.PlayCopy(c, enemy);
            }
        }));
    }

    @Override
    public boolean OnAddToDeck()
    {
        final CardGroup group = AffinityToken.CreateTokenGroup(AffinityToken.cards.size(), rng, upgraded);

        PCLGameEffects.TopLevelQueue.Callback(new SelectFromPile(name, 1, group)
        .HideTopPanel(true)
        .SetOptions(false, false)
        .CancellableFromPlayer(false)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                PCLGameEffects.TopLevelQueue.ShowAndObtain(c, InputHelper.mX, InputHelper.mY, false);
            }
        }));

        return false;
    }
}