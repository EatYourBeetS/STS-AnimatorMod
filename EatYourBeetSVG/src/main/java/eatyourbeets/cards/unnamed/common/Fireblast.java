package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Fireblast extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Fireblast.class)
            .SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .PostInitialize(data -> data.AddPreview(GR.CardLibrary.GetCard(PLAYER_CLASS, Burn.ID, false), false));

    public Fireblast()
    {
        super(DATA);

        Initialize(9, 0);
        SetUpgrade(3, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
        GameActions.Bottom.MakeCardInDrawPile(GR.CardLibrary.GetCurrentClassCard(Burn.ID, false))
        .AddCallback(card ->
        {
            if (card != null && IsSolo())
            {
                GameUtilities.MakeEthereal(card);
            }
        });
    }
}