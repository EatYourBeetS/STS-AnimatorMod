package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Strike extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Strike.class).SetAttack(1, CardRarity.BASIC);

    public Strike(String id, int cost, CardTarget target)
    {
        super(GetStaticData(id), id, AnimatorResources.GetCardImage(DATA.ID + "Alt"), cost, CardType.ATTACK, CardColor.COLORLESS,
                CardRarity.BASIC, target);

        //setBannerTexture("images\\cardui\\512\\banner_uncommon.png","images\\cardui\\1024\\banner_uncommon.png");

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
        this.tags.add(GR.Enums.CardTags.IMPROVED_STRIKE);
    }

    public Strike()
    {
        super(DATA);

        Initialize(6, 0);
        SetUpgrade(3, 0);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public AbstractCard makeCopy()
    {
        AnimatorCard copy = (AnimatorCard) super.makeCopy();
        if (GameUtilities.GetActualAscensionLevel() < 9)
        {
            copy.SetSynergy(null);
        }

        return copy;
    }
}