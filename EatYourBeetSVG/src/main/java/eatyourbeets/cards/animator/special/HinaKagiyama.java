package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.HinaKagiyamaPower;
import eatyourbeets.utilities.GameActions;

public class HinaKagiyama extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HinaKagiyama.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new HinaKagiyama_Miracle(), false);
    }

    public HinaKagiyama()
    {
        super(DATA);

        Initialize(0, 0, HinaKagiyamaPower.CARD_DRAW_AMOUNT);

        SetSynergy(Synergies.TouhouProject);
        SetAffinity(0, 0, 1, 2, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new HinaKagiyamaPower(p, 1));
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }
}