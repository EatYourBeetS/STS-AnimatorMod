package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.interfaces.delegates.ActionT3;

public class AffinityChoiceBuilder extends AnimatorCardBuilder
{
    public final Affinity affinity;
    public AffinityChoiceBuilder(Affinity affinity, int amount)
    {
        this(affinity, amount, "");
    }

    public AffinityChoiceBuilder(Affinity affinity, int amount, String text)
    {
        super(AffinityToken.GetCard(affinity), text, false);
        this.affinity = affinity;
        this.magicNumberUpgrade = this.magicNumber = amount;

    }

    public AffinityChoice Build()
    {
        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            imagePath = QuestionMark.DATA.ImagePath;
        }

        return new AffinityChoice(this);
    }

    public AffinityChoiceBuilder SetOnUse(ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        super.SetOnUse(onUseAction);
        return this;
    }
}