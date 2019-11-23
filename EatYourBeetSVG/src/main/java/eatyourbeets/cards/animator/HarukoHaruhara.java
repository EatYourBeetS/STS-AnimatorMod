package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.common.TemporaryArtifactPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class HarukoHaruhara extends AnimatorCard
{
    public static final String ID = Register(HarukoHaruhara.class.getSimpleName());

    public HarukoHaruhara()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 0);

        SetSynergy(Synergies.FLCL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        TemporaryArtifactPower.Apply(p, p, 1);

        ArrayList<String> keys = new ArrayList<>(CardLibrary.cards.keySet());

        String key;
        AbstractCard card;
        do
        {
            key = JavaUtilities.GetRandomElement(keys);
            card = CardLibrary.cards.get(key);
        }
        while (card.tags.contains(AbstractCard.CardTags.HEALING));

        card = card.makeCopy();

        if (upgraded)
        {
            card.upgrade();
        }

        card.applyPowers();

        GameActionsHelper.PlayCard(card, m);
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}