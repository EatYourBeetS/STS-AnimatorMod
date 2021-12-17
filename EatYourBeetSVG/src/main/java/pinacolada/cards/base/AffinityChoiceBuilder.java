package pinacolada.cards.base;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT3;
import pinacolada.cards.pcl.colorless.uncommon.QuestionMark;
import pinacolada.cards.pcl.tokens.AffinityToken;

public class AffinityChoiceBuilder extends PCLCardBuilder
{
    public final PCLAffinity affinity;
    public AffinityChoiceBuilder(PCLAffinity affinity, int amount)
    {
        this(affinity, amount, "");
    }

    public AffinityChoiceBuilder(PCLAffinity affinity, int amount, String text)
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

    public AffinityChoiceBuilder SetOnUse(ActionT3<PCLCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        super.SetOnUse(onUseAction);
        return this;
    }
}