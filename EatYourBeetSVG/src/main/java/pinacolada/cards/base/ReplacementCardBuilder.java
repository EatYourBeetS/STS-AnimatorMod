package pinacolada.cards.base;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.cards.pcl.colorless.QuestionMark;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class ReplacementCardBuilder extends PCLCardBuilder
{
    private static final FieldInfo<TextureAtlas.AtlasRegion> _portrait = PCLJUtils.GetField("portrait", AbstractCard.class);

    public final AbstractCard original;

    public ReplacementCardBuilder(AbstractCard card, boolean copyNumbers)
    {
        this(card, card.name, card.rawDescription, copyNumbers, true);
    }

    public ReplacementCardBuilder(AbstractCard card, String text, boolean copyNumbers)
    {
        this(card, card.name, text, copyNumbers, true);
    }

    public ReplacementCardBuilder(AbstractCard original, String name, String text, boolean copyNumbers, boolean modifyText)
    {
        super(original.cardID);
        this.original = original.makeStatEquivalentCopy();

        EYBCard eC = PCLJUtils.SafeCast(original, EYBCard.class);

        if (copyNumbers)
        {
            if (eC != null) {
                SetNumbers(eC.baseDamage, eC.baseBlock, eC.baseMagicNumber, eC.baseSecondaryValue, 1);
                SetUpgrades(eC.upgrade_damage, eC.upgrade_block, eC.upgrade_magicNumber, eC.upgrade_secondaryValue, 0);
                SetCost(eC.cost, eC.upgrade_cost);
                affinities.Initialize(PCLGameUtilities.GetPCLAffinities(eC));

                if (eC.type.equals(AbstractCard.CardType.ATTACK)) {
                    PCLAttackType at = PCLAttackType.Normal;
                    switch (eC.attackType) {
                        case None:
                            at = PCLAttackType.None;
                            break;
                        case Piercing:
                            at = PCLAttackType.Piercing;
                            break;
                        case Ranged:
                            at = PCLAttackType.Ranged;
                            break;
                        case Elemental:
                            at = PCLAttackType.Dark;
                            break;
                    }
                    SetAttackType(at, eC.attackTarget);
                }
            }
            else {
                AbstractCard upgradedCopy = original.makeStatEquivalentCopy();
                upgradedCopy.upgrade();
                SetNumbers(original.baseDamage, original.baseBlock, original.baseMagicNumber, 0, 1);
                SetUpgrades(upgradedCopy.baseDamage - original.baseDamage, upgradedCopy.baseBlock - original.baseBlock, upgradedCopy.baseMagicNumber - original.baseMagicNumber, 0, 0);
                SetCost(original.cost, upgradedCopy.cost - original.cost);
                if (original.type.equals(AbstractCard.CardType.ATTACK)) {
                    SetAttackType(PCLAttackType.Normal, original.target == AbstractCard.CardTarget.ALL_ENEMY ? EYBCardTarget.ALL : EYBCardTarget.Normal);
                }

                GetAffinitiesFromCard(original);
            }

            SetTags(original);
        }
        else
        {
            SetCost(-2, 0);
        }

        if (eC != null) {
            SetImagePath(original.assetUrl);
            if (eC instanceof AnimatorCard) {
                SetSeries(CardSeries.GetByID(((AnimatorCard) eC).series.ID));
            }
        }
        else {
            SetPortrait(_portrait.Get(original));
            SetBlockInfo(__ -> null);
            SetDamageInfo(__ -> null);
        }

        SetProperties(original.type, original.rarity, original.target);
        SetText(name, modifyText ? GetModifiedText(text) : text, null);
        SetOnUse((c, p, m) -> this.original.use(p, m));
    }

    public ReplacementCard Build()
    {
        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            imagePath = QuestionMark.DATA.ImagePath;
        }

        return new ReplacementCard(this);
    }

    protected String GetModifiedText(String originalText) {
        return originalText
                .replace("[F]", GR.Tooltips.Might.toString())
                .replace("[A]", GR.Tooltips.Velocity.toString())
                .replace("[I]", GR.Tooltips.Wisdom.toString())
                .replace("[B]", GR.Tooltips.Invocation.toString())
                .replace("[C]", GR.Tooltips.Desecration.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Force.toString(), GR.Tooltips.Might.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Agility.toString(), GR.Tooltips.Velocity.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Intellect.toString(), GR.Tooltips.Wisdom.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Blessing.toString(), GR.Tooltips.Invocation.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Corruption.toString(), GR.Tooltips.Desecration.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Affinity_Red.toString(), GR.Tooltips.Affinity_Red.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Affinity_Green.toString(), GR.Tooltips.Affinity_Green.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Affinity_Blue.toString(), GR.Tooltips.Affinity_Blue.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Affinity_Light.toString(), GR.Tooltips.Affinity_Light.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Affinity_Dark.toString(), GR.Tooltips.Affinity_Dark.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Affinity_Star.toString(), GR.Tooltips.Multicolor.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Affinity_General.toString(), GR.Tooltips.Affinity_General.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Lightning.toString(), GR.Tooltips.Lightning.toString())
                .replace(eatyourbeets.resources.GR.Tooltips.Dark.toString(), GR.Tooltips.Dark.toString());
    }

    protected void GetAffinitiesFromCard(AbstractCard original) {
        if (original.color == AbstractCard.CardColor.RED) {
            affinities.Add(PCLAffinity.Red, 1);
        }
        else if (original.color == AbstractCard.CardColor.GREEN) {
            affinities.Add(PCLAffinity.Green, 1);
        }
        else if (original.color == AbstractCard.CardColor.BLUE) {
            affinities.Add(PCLAffinity.Blue, 1);
        }
        else if (original.color == AbstractCard.CardColor.PURPLE) {
            affinities.Add(PCLAffinity.Orange, 1);
            affinities.Add(PCLAffinity.Light, 1);
        }
        else {
            String packageName = original.getClass().getPackage().getName();
            if (packageName.startsWith(GR.PackageNames.GENSOKYO_LUNAR))
            {
                affinities.Add(PCLAffinity.Light, 1);
                affinities.Add(PCLAffinity.Dark, 1);
            }
            else if (packageName.startsWith(GR.PackageNames.GENSOKYO_URBAN))
            {
                affinities.Add(PCLAffinity.Dark, 1);
            }
            else if (packageName.startsWith(GR.PackageNames.MARISA))
            {
                affinities.Add(PCLAffinity.Blue, 1);
                affinities.Add(PCLAffinity.Light, 1);
            }
        }

    }
}