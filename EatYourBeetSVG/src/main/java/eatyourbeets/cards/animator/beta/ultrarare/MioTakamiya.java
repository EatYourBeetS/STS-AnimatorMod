package eatyourbeets.cards.animator.beta.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.animator.beta.series.DateALive.ShidoItsuka;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MioTakamiya extends AnimatorCard_UltraRare implements StartupCard
{
    public static final EYBCardData DATA = Register(MioTakamiya.class).SetSkill(3, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new ShidoItsuka(), true);
    }

    public MioTakamiya()
    {
        super(DATA);

        Initialize(0, 15, 6);
        SetUpgrade(0, 0, -1);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (GameUtilities.InStance(AgilityStance.STANCE_ID))
        {
            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
            GameActions.Bottom.GainAgility(p.currentBlock / magicNumber);
        }
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);

        final ShidoItsuka shido = new ShidoItsuka();
        for (int i = 0; i < 3; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(shido)
            .SetUpgrade(upgraded, true);
        }

        return true;
    }
}